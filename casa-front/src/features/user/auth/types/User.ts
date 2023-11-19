export interface User {
    Email: string
    FirstName: string
    LastName: string
}

export interface Credentials {
    Username: string
    Password: string
}

export interface UserRegister {
    Email: string
    Password: string
    FirstName: string
    LastName: string
    File: File
}