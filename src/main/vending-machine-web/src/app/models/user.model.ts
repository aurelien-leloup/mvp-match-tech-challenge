export interface User {
  username: string,
  password: string
  deposit?: number,
  role: 'ROLE_SELLER' | 'ROLE_BUYER'

}

export interface AuthUser {
  user: User,
  jwtToken: string
}
