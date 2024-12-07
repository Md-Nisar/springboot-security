
---

# Authentication and Authorization Using JWT in Angular

## Table of Contents

1. [Introduction](#introduction)
2. [Setup Angular Project](#setup-angular-project)
3. [Core Implementation](#core-implementation)
4. [Guards](#guards)
5. [Interceptors](#interceptors)
6. [Features](#features)
7. [Future Enhancements](#future-enhancements)
8. [Folder structure](#folder-structure)


## Introduction

This guide provides a comprehensive approach to implementing authentication and authorization using JWT (JSON Web Tokens) in an Angular application. JWT is a popular method for securing web applications by transmitting information securely between the client and server as a JSON object.

## Setup Angular Project

1. **Create a new Angular project**:
   ```bash
   ng new angular-security
   cd angular-security
   ```

2. **Generate necessary components and services**:
   ```bash
   ng generate service auth
   ng generate component login
   ng generate component signup
   ng generate guard auth
   ng generate interceptor jwtAuth
   ```
   **Run application**:
   ```bash
   ng serve --open
   ```

## Core Implementation

### I.  Create Authentication Service (```AuthService```)

1. **Create methods** for:
  - Registering a new user
  - Logging in a user
  - Logging out a user
  - Checking authentication status
  - Storing and retrieving the JWT token

2. **Implement methods** to interact with your backend API for authentication.

### II. Create Guard for Protected Routes (``` AuthGuard ```)

1. **Implement the AuthGuard** to protect certain routes from being accessed by unauthenticated users.
2. **Configure canActivate** method to check for JWT token validity.

### III. HTTP Interceptor for JWT (``` JwtAuthInterceptor```)

1. **Implement HTTP interceptor** to add JWT token to the headers of outgoing HTTP requests.
2. **Handle token expiration** and redirection to the login page if the token is invalid or expired.

### IV. Create Login and Registration Components

1. **Design forms** for user login and registration.
2. **Implement form validation** for inputs.
3. **Call authentication service methods** upon form submission to authenticate or register the user.

### V. Secure Your Routes

1. **Define routes** in your `AppRoutingModule`.
2. **Protect routes** using the AuthGuard for endpoints that require authentication.
   ```typescript
   const routes: Routes = [
     { path: '', component: HomeComponent },
     { path: 'login', component: LoginComponent },
     { path: 'signup', component: SignupComponent },
     { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] }
   ];
   ```

## VI. Handle JWT Expiration

1. **Implement logic** to handle JWT expiration.
2. **Automatically refresh** tokens if your backend supports it.
3. **Redirect user** to the login page if the token has expired and cannot be refreshed.


## Guards

1. **AuthGuard**
  - **Objective**: Ensures that the user is authenticated before accessing certain routes.

2. **RoleGuard**
  - **Objective**: Ensures that the user has the required role(s) to access certain routes.

3. **CanDeactivateGuard**
  - **Objective**: Prevents navigation away from a route if there are unsaved changes.

4. **AdminGuard**
  - **Objective**: Ensures that only admin users can access certain routes.

5. **LazyLoadGuard**
  - **Objective**: Ensures that certain modules are loaded only when required.

## Interceptors

1. **AuthInterceptor**
  - **Objective**: Adds an Authorization header with a JWT token to all outgoing requests.

2. **LoggingInterceptor**
  - **Objective**: Logs details of all outgoing requests and incoming responses.

3. **ErrorInterceptor**
  - **Objective**: Handles HTTP errors globally and displays user-friendly messages.

4. **CacheInterceptor**
  - **Objective**: Caches HTTP GET requests to avoid redundant network calls.

5. **TimingInterceptor**
  - **Objective**: Measures the time taken for HTTP requests to complete.

## Features

1. **Home Page**
- **Objective**: Adds an Authorization header with a JWT token to all outgoing requests.

2. **Signup**
- **Objective**: Adds an Authorization header with a JWT token to all outgoing requests.

1. **Login**
- **Objective**: Adds an Authorization header with a JWT token to all outgoing requests.

## Folder structure

1. **Home Page**
- **Objective**: Adds an Authorization header with a JWT token to all outgoing requests.

```
src/
├── app/
│   ├── core/                          # Core module for singleton services, guards, interceptors
│   │   └── core.module.ts
│   │
│   ├── shared/                        # Shared module for common components, directives, pipes
│   │   └── shared.module.ts
│   │
│   ├── features/                      # Feature modules
│   │   ├── dashboard/                 # Dashboard feature module
│   │   │   ├── dashboard.module.ts
│   │   │   ├── components/            # Components related to dashboard
│   │   │   │   ├── widgets/           # Widgets component
│   │   │   │   │   ├── widgets.component.ts
│   │   │   │   │   ├── widgets.component.html
│   │   │   │   │   ├── widgets.component.scss
│   │   │   │   │   ├── widgets.component.spec.ts
│   │   │   │   │
│   │   │   │   ├── profile/           # Profile component
│   │   │   │   │   ├── profile.component.ts
│   │   │   │   │   ├── profile.component.html
│   │   │   │   │   ├── profile.component.scss
│   │   │   │   │   ├── profile.component.spec.ts
│   │   │   │   │
│   │   │   │   ├── change-password/   # Change password component
│   │   │   │   │   ├── change-password.component.ts
│   │   │   │   │   ├── change-password.component.html
│   │   │   │   │   ├── change-password.component.scss
│   │   │   │   │   ├── change-password.component.spec.ts
│   │   │   │   │
│   │   │   │   ├── delete-account/    # Delete account component
│   │   │   │   │   ├── delete-account.component.ts
│   │   │   │   │   ├── delete-account.component.html
│   │   │   │   │   ├── delete-account.component.scss
│   │   │   │   │   ├── delete-account.component.spec.ts
│   │   │   │   │
│   │   │   └── services/              # Services related to dashboard
│   │   │       └── dashboard.service.ts
│   │   │
│   │   └── admin/                     # Admin feature module (lazy loaded)
│   │       ├── admin.module.ts
│   │       ├── components/            # Components related to admin
│   │       │   ├── users/             # Users component
│   │       │   │   ├── users.component.ts
│   │       │   │   ├── users.component.html
│   │       │   │   ├── users.component.scss
│   │       │   │   ├── users.component.spec.ts
│   │       │   │
│   │       │   ├── roles/             # Roles component
│   │       │       ├── roles.component.ts
│   │       │       ├── roles.component.html
│   │       │       ├── roles.component.scss
│   │       │       ├── roles.component.spec.ts
│   │       │
│   │       └── services/              # Services related to admin
│   │           └── admin.service.ts
│   │
│   ├── app-routing.module.ts          # Main routing module
│   ├── app.component.ts
│   ├── app.module.ts
│   └── ...
└── ...

```

---

