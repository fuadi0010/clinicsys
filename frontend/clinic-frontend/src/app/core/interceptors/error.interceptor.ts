import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401) {
        const isAuthRoute = req.url.includes('/api/auth/login') ||
                            req.url.includes('/api/auth/register') ||
                            req.url.includes('/api/auth/verify-otp') ||
                            req.url.includes('/api/auth/resend-otp');
        if (!isAuthRoute) {
          authService.logout();
          router.navigate(['/login']);
        }
      } else if (error.status === 403) {
        console.warn('Forbidden access');
      } else if (error.status === 500) {
        console.error('Internal server error');
      }
      return throwError(() => error);
    })
  );
};
