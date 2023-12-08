package com.moin.PharmacyInventory.exceptions;

public class ResourceNotFoundException extends RuntimeException{
        public ResourceNotFoundException(String message){
            super(message);
        }
}

