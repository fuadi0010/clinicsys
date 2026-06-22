import {
  inject
} from '@angular/core';

import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router
} from '@angular/router';

import {
  AuthService
} from '../services/auth.service';

export const roleGuard:
CanActivateFn =
(route: ActivatedRouteSnapshot) => {

  const authService =
    inject(AuthService);

  const router =
    inject(Router);

  const allowedRoles = route.data['roles'];
  const currentRole = authService.getRole();

  if (currentRole && allowedRoles.includes(currentRole)) {
    return true;
  }

  if (currentRole) {
    switch (currentRole) {
      case 'PATIENT':
        router.navigate(['/dashboard/patient']);
        break;
      case 'DOCTOR':
        router.navigate(['/dashboard/doctor']);
        break;
      case 'ADMIN':
        router.navigate(['/dashboard/admin']);
        break;
      default:
        router.navigate(['/login']);
    }
  } else {
    router.navigate(['/login']);
  }

  return false;
};