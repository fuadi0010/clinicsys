export interface JwtPayload {
  sub: string;
  userId: number;
  role: string;
  iat: number;
  exp: number;
  isVerified?: boolean;
  email?: string;
}