export interface DoctorListResponse {
  id: number;
  userId: number;
  fullName: string;
  specialization: string;
  profileImageUrl?: string;
  experienceYears?: number;
  consultationFee?: number;
  rating?: number;
  averageRating?: number;
}