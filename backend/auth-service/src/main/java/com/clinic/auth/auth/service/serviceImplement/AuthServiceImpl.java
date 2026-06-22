package com.clinic.auth.auth.service.serviceImplement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clinic.auth.auth.dto.LoginRequest;
import com.clinic.auth.auth.dto.LoginResponse;
import com.clinic.auth.auth.dto.RegisterReponse;
import com.clinic.auth.auth.dto.RegisterRequest;
import com.clinic.auth.auth.dto.UserInternalResponse;
import com.clinic.auth.exception.UserNotFoundException;
import com.clinic.auth.exception.UserAlreadyExistsException;
import com.clinic.auth.exception.InvalidPasswordException;
import com.clinic.auth.exception.InvalidTokenException;
import com.clinic.auth.exception.RoleNotFoundException;
import com.clinic.auth.exception.AccountDisabledException;
import com.clinic.auth.common.exception.BadRequestException;
import com.clinic.auth.auth.mapper.AuthMapper;
import com.clinic.auth.auth.otp.dto.ResendOtpRequest;
import com.clinic.auth.auth.otp.dto.VerifyOtpRequest;
import com.clinic.auth.auth.otp.email.EmailService;
import com.clinic.auth.auth.otp.entity.OtpEntity;
import com.clinic.auth.auth.otp.repository.OtpRepository;
import com.clinic.auth.auth.otp.util.OtpGenerator;
import com.clinic.auth.auth.refreshToken.entity.RefreshTokenEntity;
import com.clinic.auth.auth.refreshToken.repository.RefreshTokenRepository;
import com.clinic.auth.auth.service.AuthService;
import com.clinic.auth.role.entity.RoleEntity;
import com.clinic.auth.role.repository.RoleRepository;
import com.clinic.auth.security.jwt.JwtService;
import com.clinic.auth.user.entity.UsersEntity;
import com.clinic.auth.user.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final UsersRepository usersRepository;
        private final RoleRepository roleRepository;
        private final AuthMapper authMapper;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final OtpRepository otpRepository;
        private final EmailService emailService;
        private final RefreshTokenRepository refreshTokenRepository;

        @Override
        public RegisterReponse register(RegisterRequest request) {

                if (usersRepository.existsByUsername(request.getUsernameRequest())) {
                        throw new UserAlreadyExistsException("Username sudah digunakan!");
                }
                if (usersRepository.existsByEmail(request.getEmailRequest())) {
                        throw new UserAlreadyExistsException("Email sudah terdaftar!");
                }

                UsersEntity newUser = authMapper.toEntity(request);

                newUser.setPassword(passwordEncoder.encode(request.getPasswordRequest()));

                String requestedRole = request.getRole() == null
                                ? "PATIENT"
                                : request.getRole().trim().toUpperCase();

                if (!requestedRole.equals("PATIENT") && !requestedRole.equals("DOCTOR")) {
                        requestedRole = "PATIENT";
                }

                RoleEntity role = roleRepository
                                .findByRoleName(requestedRole)
                                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

                newUser.setRole(role);
                newUser.setIsActive(true);
                newUser = usersRepository.save(newUser);

                String otp = OtpGenerator.generateOtp();

                OtpEntity otpEntity = OtpEntity.builder().email(request.getEmailRequest()).otpCode(otp)
                                .expiredAt(LocalDateTime.now().plusMinutes(5)).used(false).build();

                otpRepository.save(otpEntity);

                // Print OTP to stdout as fallback for development/testing
                System.out.println("========================================");
                System.out.println("OTP CODE FOR " + request.getEmailRequest() + " IS: " + otp);
                System.out.println("========================================");

                try {
                        emailService.sendOtp(request.getEmailRequest(), otp);
                } catch (Exception e) {
                        System.err.println("Failed to send OTP email to " + request.getEmailRequest() + ": " + e.getMessage());
                }

                return authMapper.toResponse(newUser);
        }

        @Override
        public LoginResponse login(LoginRequest request) {

                UsersEntity user = usersRepository.findByUsername(request.getUsername())
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        // Fallback: check if matches plaintext (for legacy accounts like doctor1)
                        if (request.getPassword().equals(user.getPassword())) {
                                // Auto-upgrade legacy password to BCrypt hash
                                user.setPassword(passwordEncoder.encode(request.getPassword()));
                                usersRepository.save(user);
                        } else {
                                throw new InvalidPasswordException("Invalid password");
                        }
                }

                if (user.getIsActive() == null || !user.getIsActive()) {
                        throw new AccountDisabledException("Akun Anda telah diblokir oleh Admin. Silakan hubungi layanan pelanggan.");
                }

                // Allow unverified users to login to access their profile and perform verification
                if (user.getIsVerified() == null || !user.getIsVerified()) {
                        throw new AccountDisabledException("Account not verified");
                }
                String accessToken = jwtService.generateToken(user);

                String refreshTokenValue = UUID.randomUUID().toString();
                RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                        .token(refreshTokenValue)
                        .userId(user.getId())
                        .revoked(false)
                        .expiresAt(LocalDateTime.now().plusDays(7))
                        .createdAt(LocalDateTime.now())
                        .build();
                refreshTokenRepository.save(refreshToken);

                return LoginResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshTokenValue)
                                .tokenType("Bearer")
                                .build();
        }

        @Override
        public String verifyOtp(VerifyOtpRequest request) {
                OtpEntity otpEntity = otpRepository.findTopByEmailAndUsedFalseOrderByIdDesc(request.getEmail())
                                .orElseThrow(() -> new InvalidTokenException("OTP not found"));
                if (LocalDateTime.now().isAfter(otpEntity.getExpiredAt())) {
                        throw new InvalidTokenException("OTP expired");
                }
                if (!otpEntity.getOtpCode().equals(request.getOtp())) {
                        throw new InvalidTokenException("Invalid OTP");
                }
                UsersEntity user = usersRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new UserNotFoundException("User not found"));
                user.setIsVerified(true);

                usersRepository.save(user);

                otpEntity.setUsed(true);

                otpRepository.save(otpEntity);

                return "ACCOUNT VERIFIED";
        }

        @Override
        public String resendOtp(
                        ResendOtpRequest request) {

                UsersEntity user = usersRepository
                                .findByEmail(
                                                 request.getEmail())
                                .orElseThrow(
                                                 () -> new UserNotFoundException(
                                                                 "User not found"));

                if (Boolean.TRUE.equals(
                                user.getIsVerified())) {

                        throw new BadRequestException(
                                        "Account already verified");
                }

                List<OtpEntity> activeOtps = otpRepository
                                .findByEmailAndUsedFalse(
                                                 request.getEmail());

                activeOtps.forEach(
                                otp -> otp.setUsed(true));

                otpRepository.saveAll(
                                activeOtps);

                String otp = OtpGenerator.generateOtp();

                OtpEntity otpEntity = OtpEntity.builder()
                                .email(user.getEmail())
                                .otpCode(otp)
                                .used(false)
                                .expiredAt(
                                                 LocalDateTime.now()
                                                                 .plusMinutes(5))
                                .build();

                otpRepository.save(
                                otpEntity);

                emailService.sendOtp(
                                user.getEmail(),
                                otp);

                return "OTP sent successfully";
        }

        @Override
        public UserInternalResponse findById(Long id) {

                UsersEntity user = usersRepository.findById(id)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

                UserInternalResponse response = new UserInternalResponse();

                response.setId(user.getId());
                response.setUsername(user.getUsername());
                response.setEmail(user.getEmail());

                return response;
        }

        @Override
        public LoginResponse refreshToken(String refreshTokenValue) {
                RefreshTokenEntity refreshToken = refreshTokenRepository
                        .findByTokenAndRevokedFalse(refreshTokenValue)
                        .orElseThrow(() -> new InvalidTokenException("Invalid or expired refresh token"));

                if (LocalDateTime.now().isAfter(refreshToken.getExpiresAt())) {
                        refreshToken.setRevoked(true);
                        refreshTokenRepository.save(refreshToken);
                        throw new InvalidTokenException("Refresh token expired");
                }

                UsersEntity user = usersRepository.findById(refreshToken.getUserId())
                        .orElseThrow(() -> new UserNotFoundException("User not found"));

                String newAccessToken = jwtService.generateToken(user);

                String newRefreshTokenValue = UUID.randomUUID().toString();
                refreshToken.setRevoked(true);
                refreshTokenRepository.save(refreshToken);

                RefreshTokenEntity newRefreshToken = RefreshTokenEntity.builder()
                        .token(newRefreshTokenValue)
                        .userId(user.getId())
                        .revoked(false)
                        .expiresAt(LocalDateTime.now().plusDays(7))
                        .createdAt(LocalDateTime.now())
                        .build();
                refreshTokenRepository.save(newRefreshToken);

                return LoginResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshTokenValue)
                        .tokenType("Bearer")
                        .build();
        }

        @Override
        public void logout(String refreshTokenValue) {
                refreshTokenRepository.findByTokenAndRevokedFalse(refreshTokenValue)
                        .ifPresent(token -> {
                                token.setRevoked(true);
                                refreshTokenRepository.save(token);
                        });
        }

        @Override
        @org.springframework.transaction.annotation.Transactional
        public UserInternalResponse registerDoctorInternal(RegisterRequest request) {
                if (usersRepository.existsByUsername(request.getUsernameRequest())) {
                        throw new UserAlreadyExistsException("Username sudah digunakan!");
                }
                if (usersRepository.existsByEmail(request.getEmailRequest())) {
                        throw new UserAlreadyExistsException("Email sudah terdaftar!");
                }

                UsersEntity newUser = new UsersEntity();
                newUser.setUsername(request.getUsernameRequest());
                newUser.setEmail(request.getEmailRequest());
                newUser.setPassword(passwordEncoder.encode(request.getPasswordRequest()));

                RoleEntity role = roleRepository
                                .findByRoleName("DOCTOR")
                                .orElseThrow(() -> new RoleNotFoundException("Role DOCTOR not found"));

                newUser.setRole(role);
                newUser.setIsActive(true);
                newUser.setIsVerified(true);

                newUser = usersRepository.save(newUser);

                UserInternalResponse response = new UserInternalResponse();
                response.setId(newUser.getId());
                response.setUsername(newUser.getUsername());
                response.setEmail(newUser.getEmail());
                return response;
        }

        @Override
        @org.springframework.transaction.annotation.Transactional
        public void deleteUserInternal(Long id) {
                UsersEntity user = usersRepository.findById(id)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));
                
                // Hapus refresh token terkait dulu untuk menghindari foreign key / constraint violation
                List<RefreshTokenEntity> tokens = refreshTokenRepository.findByUserId(id);
                if (tokens != null && !tokens.isEmpty()) {
                        refreshTokenRepository.deleteAll(tokens);
                }
                
                usersRepository.delete(user);
        }

        @Override
        @org.springframework.transaction.annotation.Transactional
        public void deactivateUserInternal(Long id) {
                UsersEntity user = usersRepository.findById(id)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));
                user.setIsActive(false);
                usersRepository.save(user);
        }
}
