import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { DoctorListResponse } from '../models/doctor/doctor-list-response.model';
import { DoctorDetailResponse } from '../models/doctor/doctor-detail-response.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private readonly http = inject(HttpClient);
  private readonly API = `${environment.apiUrl}/api/doctors`;

  getAllDoctors(): Observable<DoctorListResponse[]> {
    return this.http.get<ApiResponse<DoctorListResponse[]>>(this.API).pipe(
      map(res => res.data)
    );
  }

  getDoctorById(doctorId: number): Observable<DoctorDetailResponse> {
    return this.http.get<ApiResponse<DoctorDetailResponse>>(`${this.API}/${doctorId}`).pipe(
      map(res => res.data)
    );
  }

  createDoctorByAdmin(request: any, file: File | null): Observable<DoctorDetailResponse> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(request)], { type: 'application/json' }));
    if (file) {
      formData.append('file', file);
    }
    return this.http.post<ApiResponse<DoctorDetailResponse>>(`${this.API}/admin/create`, formData).pipe(
      map(res => res.data)
    );
  }

  getMyProfile(): Observable<DoctorDetailResponse> {
    return this.http.get<ApiResponse<DoctorDetailResponse>>(`${this.API}/me`).pipe(
      map(res => res.data)
    );
  }

  getDoctorsCount(): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${this.API}/count`).pipe(
      map(res => res.data)
    );
  }
}