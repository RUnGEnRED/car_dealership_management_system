export interface AuthResponse {
  token: string;
  id: number;
  username: string;
  email: string;
  roles: string[];
}

export interface UseLoginResult {
  username: string;
  setUsername: React.Dispatch<React.SetStateAction<string>>;
  password: string;
  setPassword: React.Dispatch<React.SetStateAction<string>>;
  error: string | null;
  loading: boolean;
  handleLogin: (e: React.FormEvent) => Promise<void>;
}