import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class ContactMessageService {
  private readonly http = inject(HttpClient);
  private readonly API = `${environment.apiUrl}/api/v1/contact-messages`;

  sendContactMessage(request: any): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(this.API, request);
  }
}
