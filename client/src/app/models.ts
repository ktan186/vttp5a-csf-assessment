// You may use this file to create any models

export interface Menu {
    id: string, 
    name: string, 
    description: string, 
    price: number,
    quantity: number
}

export interface User {
    username: string, 
    password: string
}

export interface Order {
    id: string, 
    price: number, 
    quantity: number
}