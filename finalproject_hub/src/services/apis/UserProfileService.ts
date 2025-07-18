const mockUserProfile = {
  id: 'current-user-id',
  fullName: 'John Doe',
  age: 28,
  email: 'john.doe@example.com',
  phoneNumber: '+995 555 123 456',
  address: '123 Rustaveli Ave, Tbilisi, Georgia',
  emergencyContact: {
    name: 'Jane Doe',
    relationship: 'Spouse',
    phone: '+995 555 123 457',
    email: 'jane.doe@example.com'
  },
  medicalConditions: 'None',
  bloodType: 'O+',
  previousExperience: 'intermediate',
  skillLevel: 'advanced',
  previousAchievements: 'Local tennis champion 2023',
  tshirtSize: 'L',
  dietaryRestrictions: 'None',
  specialRequirements: 'None'
}

export interface UserProfile {
  id: string
  fullName: string
  age: number
  email: string
  phoneNumber: string
  address: string
  emergencyContact: {
    name: string
    relationship: string
    phone: string
    email: string
  }
  medicalConditions?: string
  bloodType?: string
  previousExperience?: string
  skillLevel?: string
  previousAchievements?: string
  tshirtSize?: string
  dietaryRestrictions?: string
  specialRequirements?: string
}

export class UserProfileService {
  static async getUserProfile(): Promise<UserProfile> {
    try {
      await new Promise(resolve => setTimeout(resolve, 300))
      return { ...mockUserProfile }
    } catch (error) {
      console.error('Error getting user profile:', error)
      throw error
    }
  }

  static async updateUserProfile(profileData: Partial<UserProfile>): Promise<UserProfile> {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // In real app, this would update the backend
      Object.assign(mockUserProfile, profileData)
      
      return { ...mockUserProfile }
    } catch (error) {
      console.error('Error updating user profile:', error)
      throw error
    }
  }
} 