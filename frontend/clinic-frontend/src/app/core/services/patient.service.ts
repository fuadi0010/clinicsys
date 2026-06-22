import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { PatientResponse } from '../models/patient/patient-response.model';
import { UpdatePatientRequest } from '../models/patient/update-patient-request.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private http = inject(HttpClient);
  private readonly API = `${environment.apiUrl}/api/patients`;

  getMyProfile(): Observable<PatientResponse> {
    return this.http.get<ApiResponse<PatientResponse>>(`${this.API}/me`).pipe(
      map(res => res.data)
    );
  }

  createProfile(request: UpdatePatientRequest): Observable<PatientResponse> {
    return this.http.post<ApiResponse<PatientResponse>>(`${this.API}/create`, request).pipe(
      map(res => res.data)
    );
  }

  updateProfile(request: UpdatePatientRequest): Observable<PatientResponse> {
    return this.http.patch<ApiResponse<PatientResponse>>(`${this.API}/me`, request).pipe(
      map(res => res.data)
    );
  }

  uploadProfileImage(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<ApiResponse<string>>(`${this.API}/profile-image`, formData).pipe(
      map(res => res.data)
    );
  }

  /** Admin: get all patients */
  getAllPatients(): Observable<PatientResponse[]> {
    return this.http.get<ApiResponse<PatientResponse[]>>(this.API).pipe(
      map(res => res.data)
    );
  }

  getPatientById(id: number): Observable<PatientResponse> {
    return this.http.get<ApiResponse<PatientResponse>>(`${this.API}/${id}`).pipe(
      map(res => res.data)
    );
  }

  /** Admin: delete patient */
  deletePatient(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.API}/${id}`).pipe(
      map(res => res.data)
    );
  }

  getPatientsCount(): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${this.API}/count`).pipe(
      map(res => res.data)
    );
  }

  submitRating(request: { appointmentId: number; doctorId: number; rating: number; review: string }): Observable<any> {
    return this.http.post<ApiResponse<any>>(`${this.API}/ratings`, request).pipe(
      map(res => res.data)
    );
  }

  getMyRatings(): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(`${this.API}/ratings/me`).pipe(
      map(res => res.data)
    );
  }
}