export interface DoctorDetailResponse {
  id: number;
  userId: number;
  fullName: string;
  specialization: string;
  strNumber: string;
  phoneNumber: string;
  bio: string;
  consultationFee: number;
  profileImageUrl?: string;
  experienceYears?: number;
  rating?: number;
  averageRating?: number;
}