package com.moin.DoctorProfile.exceptions;

public class ResourceNotFoundException extends RuntimeException{
        public ResourceNotFoundException(String message){
            super(message);
        }
}

