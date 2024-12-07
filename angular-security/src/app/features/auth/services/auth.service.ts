// shared/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHandler, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { AuthResponse, JwtResponse, LoginRequest, LoginResponse, SignupRequest, SignupResponse } from 'src/app/models/auth.model';
import { ApiEndpoint as API } from 'src/app/constants/api.constant';
import { ApiResponse } from 'src/app/models/api-response.model';
import { JwtTokenService } from './jwt-token.service';
import { UserService } from 'src/app/@shared/services/user.service';
import { Roles } from 'src/app/constants/roles.enum';
import { HttpHeaders as Headers } from 'src/app/constants/http.constant'

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly baseUrl: string;
  private loggedInSubject: BehaviorSubject<boolean>;
  public loggedIn$: Observable<boolean>;

  constructor(
    private http: HttpClient,
    private tokenService: JwtTokenService,
    private userService: UserService,
  ) {
    this.baseUrl = environment.serverHost + environment.basePath;
    this.loggedInSubject = new BehaviorSubject<boolean>(this.tokenService.hasValidToken());
    this.loggedIn$ = this.loggedInSubject.asObservable();
  }

  public isLoggedIn(): boolean {
    return this.loggedInSubject.value;
  }

  public signup(signupData: SignupRequest): Observable<SignupResponse> {
    delete signupData.confirmPassword;
    return this.http.post<ApiResponse<SignupResponse>>(`${this.baseUrl}${API.SIGNUP_ENDPOINT}`, signupData)
      .pipe(
        map((response) => response.data ?? {} as SignupResponse),
        catchError(this.handleError)
      );
  }

  public verify(username: string, token: string): Observable<void> {
     let params = new HttpParams()
     .set('username', encodeURI(username))
     .set('token', encodeURI(token));
                                
    return this.http.get<ApiResponse<void>>(`${this.baseUrl}${API.VERIFY_ENDPOINT}`, { params })
      .pipe(
        map((response) => response.data),
        catchError(this.handleError)
      );
  }

  public login(loginData: LoginRequest): Observable<AuthResponse> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.baseUrl}${API.LOGIN_ENDPOINT}`, loginData)
      .pipe(
        tap(response => console.log(response.message)),
        map(response => response.data ?? {} as AuthResponse),
        tap(response => {
          this.tokenService.setAccessToken(response.accessToken);
          this.tokenService.setRefreshToken(response.refreshToken);
          this.loggedInSubject.next(true);
        })
      );
  }

  public logout(): Observable<ApiResponse<any>> {
    const refreshToken = this.tokenService.getRefreshToken();

    const headers = new HttpHeaders({
      [Headers.X_REFRESH_TOKEN]: `Bearer ${refreshToken}` || ''
    });

    return this.http.post<ApiResponse<any>>(`${this.baseUrl}${API.LOGOUT_ENDPOINT}`, {}, { headers })
      .pipe(
        tap(() => {
          this.tokenService.removeTokens();
          this.userService.clearUserDetails();
          this.loggedInSubject.next(false);
        })
      );
  }

  public canLoadModule(requiredRole?: Roles): boolean {
    if (!this.loggedInSubject.value) {
      return false;
    }

    if (requiredRole) {
      return this.userService.hasRole(requiredRole);
    }

    return true;
  }

  private handleError(error: any): Observable<never> {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side errors
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side errors
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(() => new Error(errorMessage));
  }
}
