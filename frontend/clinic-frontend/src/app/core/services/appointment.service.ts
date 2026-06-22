import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { CreateAppointmentRequest } from '../models/appointment/create-appointment-request.model';
import { AppointmentResponse } from '../models/appointment/appointment-response.model';
import { UpdateAppointmentStatusRequest } from '../models/appointment/update-appointment-status-request.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private readonly http = inject(HttpClient);
  private readonly API = `${environment.apiUrl}/api/appointments`;

  createAppointment(request: CreateAppointmentRequest): Observable<AppointmentResponse> {
    return this.http.post<ApiResponse<AppointmentResponse>>(`${this.API}/create`, request).pipe(
      map(res => res.data)
    );
  }

  getMyAppointments(): Observable<AppointmentResponse[]> {
    return this.http.get<ApiResponse<AppointmentResponse[]>>(`${this.API}/me`).pipe(
      map(res => res.data)
    );
  }

  getDoctorAppointments(): Observable<AppointmentResponse[]> {
    return this.http.get<ApiResponse<AppointmentResponse[]>>(`${this.API}/doctor/me`).pipe(
      map(res => res.data)
    );
  }

  getAppointmentById(id: number): Observable<AppointmentResponse> {
    return this.http.get<ApiResponse<AppointmentResponse>>(`${this.API}/${id}`).pipe(
      map(res => res.data)
    );
  }

  getAppointmentForPatient(id: number): Observable<AppointmentResponse> {
    return this.http.get<ApiResponse<AppointmentResponse>>(`${this.API}/patient/detail/${id}`).pipe(
      map(res => res.data)
    );
  }

  updateStatus(appointmentId: number, request: UpdateAppointmentStatusRequest): Observable<AppointmentResponse> {
    return this.http.patch<ApiResponse<AppointmentResponse>>(`${this.API}/${appointmentId}/status`, request).pipe(
      map(res => res.data)
    );
  }

  getAvailableSlots(doctorId: number, date: string): Observable<{ time: string, available: boolean }[]> {
    return this.http.get<ApiResponse<{ time: string, available: boolean }[]>>(`${this.API}/available-slots`, {
      params: { doctorId: doctorId.toString(), date }
    }).pipe(
      map(res => res.data)
    );
  }

  getAppointmentStatus(id: number): Observable<{ status: string }> {
    return this.http.get<ApiResponse<{ status: string }>>(`${this.API}/${id}/status`).pipe(
      map(res => res.data)
    );
  }

  getAllAppointments(): Observable<AppointmentResponse[]> {
    return this.http.get<ApiResponse<AppointmentResponse[]>>(`${this.API}`).pipe(
      map(res => res.data)
    );
  }

  getActiveAppointmentsCount(): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${this.API}/count/active`).pipe(
      map(res => res.data)
    );
  }
}