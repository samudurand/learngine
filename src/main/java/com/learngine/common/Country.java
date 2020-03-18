package com.learngine.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

//TODO consider using Locale or something containing iso 3166-1
public enum Country {
    FR, // France
    GB, // Great Britain
    IT, // Italy
    ES, // Spain
    US, // United States

    @JsonEnumDefaultValue
    UKNOWN // For all country codes not handled
}
