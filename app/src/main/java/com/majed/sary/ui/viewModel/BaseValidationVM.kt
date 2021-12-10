package com.majed.sary.ui.viewModel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.majed.sary.R
import com.majed.sary.data.consts.Params
import com.majed.sary.data.model.modified.ErrorHandler
import java.util.*
import java.util.regex.Pattern

open class BaseValidationVM : ViewModel() {

    var formState: MutableLiveData<ErrorHandler> = MutableLiveData()

    //------------------------------------------- Validation Values & Methods ----------------------

    private val regex = Pattern.compile("[0-9]+").toRegex()

    private fun isFieldRequired(field: String?) = field.isNullOrEmpty()

    private fun isFieldLessThanTwenty(field: String) = field.trim().toInt() < 20

    private fun isFieldLessThanFifteen(field: String) = field.trim().length < 15

    private fun isFieldLessThanFive(input: String) = input.trim().length < 5

    private fun isFieldLessThanSix(input: String) = input.trim().length < 6

    private fun isFieldLessThanFour(input: String) = input.trim().length < 4

    private fun isFieldLessThanThree(input: String) = input.trim().length < 3

    private fun isFieldLessThanTwo(input: String) = input.trim().length < 2

    private fun isFieldLessThanOne(input: String) = input.trim().isEmpty()

    private fun isFieldEqualZero(input: String) = input.trim().toFloat() == 0f

    private fun isFieldNotUrl(field: String?): Boolean {
        val validUrlRegex = Patterns.WEB_URL
        if (field.isNullOrEmpty())
            return true

        return !validUrlRegex.matcher(field).find()
    }

    fun isPhoneNotValid(phone: String?): Boolean {
        val validPhoneRegex = Pattern.compile("^[+]?[0-9]{10,11}$")
        if (phone.isNullOrEmpty())
            return true

        return !validPhoneRegex.matcher(phone).find()
    }

    private fun isLatNotValid(lat: String?): Boolean {
        val validLatRegex =
            Pattern.compile("(^\\+?([1-8])?\\d(\\.\\d+)?$)|(^-90$)|(^-(([1-8])?\\d(\\.\\d+)?$))")
        if (lat.isNullOrEmpty())
            return true

        return !validLatRegex.matcher(lat).find()
    }

    private fun isLngNotValid(lng: String?): Boolean {
        val validLngRegex =
            Pattern.compile("(^\\+?1[0-7]\\d(\\.\\d+)?$)|(^\\+?([1-9])?\\d(\\.\\d+)?$)|(^-180$)|(^-1[1-7]\\d(\\.\\d+)?$)|(^-[1-9]\\d(\\.\\d+)?$)|(^-\\d(\\.\\d+)?$)")
        if (lng.isNullOrEmpty())
            return true

        return !validLngRegex.matcher(lng).find()
    }

    private fun isPostalCodeNotValid(postalCode: String?): Boolean {
        val validPhoneRegex = Pattern.compile("^[+]?[0-9]{5,9}$")
        if (postalCode.isNullOrEmpty())
            return true

        return !validPhoneRegex.matcher(postalCode).find()
    }

    private fun isEmailNotValid(email: String?): Boolean {
        if (email == null) return false
        return if (email.contains("@")) {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else email.trim().isNotEmpty()
    }

    private fun isPasswordNotValid(password: String?): Boolean =
        password.isNullOrEmpty() || password.trim().length < 4

    private fun isPriceNotValid(price: String?): Boolean =
        price.isNullOrEmpty() || price.trim().toInt() == 0

    private fun isPriceLessThanFifty(price: String?): Boolean =
        price.isNullOrEmpty() || price.trim().toInt() < 50

    open fun getStringMaxLength(txt: String): String =
        if (txt.length > 15) txt.substring(0, 15) else txt

    //------------------------------------- Input Validation Methods -------------------------------

    protected fun validateInputIsRequiredAndSixChars(
        fieldValue: String, fieldKey: String
    ): Boolean = when {
        isFieldRequired(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.required)
            false
        }
        isFieldLessThanSix(fieldValue) -> {
            formState.value =
                ErrorHandler(fieldKey, R.string.should_six_chars)
            false
        }
        else -> {
            formState.value = ErrorHandler(fieldKey)
            true
        }
    }

    protected fun validateInputIsRequiredAndFiveChars(
        fieldValue: String, fieldKey: String
    ): Boolean = when {
        isFieldRequired(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.required)
            false
        }
        isFieldLessThanFive(fieldValue) -> {
            formState.value =
                ErrorHandler(fieldKey, R.string.should_five_chars)
            false
        }
        else -> {
            formState.value = ErrorHandler(fieldKey)
            true
        }
    }

    protected fun validateInputIsRequiredAndThreeChars(
        fieldValue: String, fieldKey: String
    ): Boolean = when {
        isFieldRequired(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.required)
            false
        }
        isFieldLessThanThree(fieldValue) -> {
            formState.value =
                ErrorHandler(fieldKey, R.string.should_three_chars)
            false
        }
        else -> {
            formState.value = ErrorHandler(fieldKey)
            true
        }
    }

    protected fun validateInputIsRequiredAndFourChars(
        fieldValue: String, fieldKey: String
    ): Boolean = when {
        isFieldRequired(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.required)
            false
        }
        isFieldLessThanFour(fieldValue) -> {
            formState.value =
                ErrorHandler(fieldKey, R.string.should_four_chars)
            false
        }
        else -> {
            formState.value = ErrorHandler(fieldKey)
            true
        }
    }

    protected fun validateInputIsRequiredAndTwoChars(
        fieldValue: String, fieldKey: String
    ): Boolean = when {
        isFieldRequired(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.required)
            false
        }
        isFieldLessThanTwo(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.should_two_chars)
            false
        }
        else -> {
            formState.value = ErrorHandler(fieldKey)
            true
        }
    }

    protected fun validateInputIsRequiredAndOneChars(
        fieldValue: String, fieldKey: String
    ): Boolean = when {
        isFieldRequired(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.required)
            false
        }
        isFieldLessThanOne(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.should_one_chars)
            false
        }
        else -> {
            formState.value = ErrorHandler(fieldKey)
            true
        }
    }

    protected fun validateInputIsRequiredAndNotZero(
        fieldValue: String, fieldKey: String
    ): Boolean = when {
        isFieldRequired(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.required)
            false
        }
        isFieldEqualZero(fieldValue) -> {
            formState.value = ErrorHandler(fieldKey, R.string.should_not_zero)
            false
        }
        else -> {
            formState.value = ErrorHandler(fieldKey)
            true
        }
    }

    protected fun validateInputIsThreeChars(fieldValue: String, fieldKey: String): Boolean {
        return when {
            !isFieldRequired(fieldValue) -> {
                if (isFieldLessThanThree(fieldValue)) {
                    formState.value =
                        ErrorHandler(fieldKey, R.string.should_three_chars)
                    false
                } else {
                    formState.value = ErrorHandler(fieldKey)
                    true
                }
            }
            else -> true
        }
    }

    protected fun validateInputIsFourChars(fieldValue: String, fieldKey: String): Boolean {
        return when {
            !isFieldRequired(fieldValue) -> {
                if (isFieldLessThanThree(fieldValue)) {
                    formState.value =
                        ErrorHandler(fieldKey, R.string.should_three_chars)
                    false
                } else {
                    formState.value = ErrorHandler(fieldKey)
                    true
                }
            }
            else -> true
        }
    }

    protected fun validateInputIsTwoChars(fieldValue: String, fieldKey: String): Boolean {
        return when {
            !isFieldRequired(fieldValue) -> {
                if (isFieldLessThanTwo(fieldValue)) {
                    formState.value =
                        ErrorHandler(fieldKey, R.string.should_two_chars)
                    false
                } else {
                    formState.value = ErrorHandler(fieldKey)
                    true
                }
            }
            else -> true
        }
    }

    protected fun validateInputIsUrl(fieldValue: String, fieldKey: String): Boolean {
        return when {
            !isFieldRequired(fieldValue) -> {
                if (isFieldNotUrl(fieldValue)) {
                    formState.value =
                        ErrorHandler(fieldKey, R.string.invalid_url_format)
                    false
                } else {
                    formState.value = ErrorHandler(fieldKey)
                    true
                }
            }
            else -> true
        }
    }

    protected fun validateInputIsRequired(fieldValue: String, fieldKey: String): Boolean {
        return when {
            isFieldRequired(fieldValue) -> {
                formState.value = ErrorHandler(fieldKey, R.string.required)
                false
            }
            else -> {
                formState.value = ErrorHandler(fieldKey)
                true
            }
        }
    }

    protected fun validateFieldIsRequired(fieldValue: Boolean, fieldKey: String): Boolean {
        return when (fieldValue) {
            false -> {
                formState.value = ErrorHandler(fieldKey, R.string.required)
                false
            }
            else -> {
                formState.value = ErrorHandler(fieldKey)
                true
            }
        }
    }

    protected fun validatePhoneInputAndNotRequired(phone: String): Boolean = when {
        !isFieldRequired(phone) -> {
            if (isPhoneNotValid(phone)) {
                formState.value =
                    ErrorHandler(Params.PHONE, R.string.phone_not_valid)
                false
            } else {
                formState.value = ErrorHandler(Params.PHONE)
                true
            }
        }
        else -> true
    }

    protected fun validatePhoneInput(phone: String): Boolean = when {
        isFieldRequired(phone) -> {
            formState.value = ErrorHandler(Params.PHONE, R.string.required)
            false
        }
        isPhoneNotValid(phone) -> {
            formState.value = ErrorHandler(Params.PHONE, R.string.phone_not_valid)
            false
        }
        else -> {
            formState.value = ErrorHandler(Params.PHONE)
            true
        }
    }

    protected fun validateEmailInput(email: String): Boolean = when {
        isFieldRequired(email) -> {
            formState.value = ErrorHandler(Params.EMAIL, R.string.required)
            false
        }
        isEmailNotValid(email) -> {
            formState.value = ErrorHandler(Params.EMAIL, R.string.email_not_valid)
            false
        }
        else -> {
            formState.value = ErrorHandler(Params.EMAIL)
            true
        }
    }

    protected fun validatePasswordInput(password: String, fieldKey: String): Boolean = when {
        isFieldRequired(password) -> {
            formState.value = ErrorHandler(fieldKey, R.string.required)
            false
        }
        isPasswordNotValid(password) -> {
            formState.value = ErrorHandler(fieldKey, R.string.should_four_chars)
            false
        }
        else -> {
            formState.value = ErrorHandler(fieldKey)
            true
        }
    }

    protected fun validateRepeatNewPasswordInput(
        newPassword: String, repeatNewPassword: String, fieldKey: String
    ): Boolean = when {
        isFieldRequired(repeatNewPassword) -> {
            formState.value = ErrorHandler(fieldKey, R.string.required)
            false
        }
        isPasswordNotValid(repeatNewPassword) -> {
            formState.value =
                ErrorHandler(fieldKey, R.string.should_four_chars)
            false
        }
        !Objects.equals(newPassword, repeatNewPassword) -> {
            formState.value = ErrorHandler(fieldKey, R.string.repeat_password_not_matches)
            false
        }
        else -> {
            formState.value = ErrorHandler(fieldKey)
            true
        }
    }

    protected fun validatePhoneEmailInput(phone_email: String): Boolean {
        return when {
            isFieldRequired(phone_email) -> {
                formState.value = ErrorHandler(Params.PHONE_EMAIL, R.string.required)
                false
            }
            else -> {
                if (isEmailNotValid(phone_email) && !phone_email.matches(regex)) {
                    formState.value =
                        ErrorHandler(Params.PHONE_EMAIL, R.string.email_not_valid)
                    false
                } else if (isPhoneNotValid(phone_email) && phone_email.matches(regex)) {
                    formState.value =
                        ErrorHandler(Params.PHONE_EMAIL, R.string.phone_not_valid)
                    false
                } else {
                    formState.value = ErrorHandler(Params.PHONE_EMAIL)
                    true
                }
            }
        }
    }

    protected fun validateLatInput(lat: String, required: Boolean): Boolean {
        return if (required) {
            when {
                isFieldRequired(lat) -> {
                    formState.value = ErrorHandler(Params.LAT, R.string.required)
                    false
                }
                isLatNotValid(lat) -> {
                    formState.value =
                        ErrorHandler(Params.LAT, R.string.lat_is_not_valid)
                    false
                }
                else -> {
                    formState.value = ErrorHandler(Params.LAT)
                    true
                }
            }
        } else {
            when {
                !isFieldRequired(lat) -> {
                    if (isLatNotValid(lat)) {
                        formState.value =
                            ErrorHandler(Params.LAT, R.string.lat_is_not_valid)
                        false
                    } else {
                        formState.value = ErrorHandler(Params.LAT)
                        true
                    }
                }
                else -> true
            }
        }
    }

    protected fun validateLngInput(lng: String, required: Boolean): Boolean {
        return if (required) {
            when {
                isFieldRequired(lng) -> {
                    formState.value = ErrorHandler(Params.LNG, R.string.required)
                    false
                }
                isLngNotValid(lng) -> {
                    formState.value =
                        ErrorHandler(Params.LNG, R.string.lng_is_not_valid)
                    false
                }
                else -> {
                    formState.value = ErrorHandler(Params.LNG)
                    true
                }
            }
        } else {
            when {
                !isFieldRequired(lng) -> {
                    if (isLngNotValid(lng)) {
                        formState.value =
                            ErrorHandler(Params.LNG, R.string.lng_is_not_valid)
                        false
                    } else {
                        formState.value = ErrorHandler(Params.LNG)
                        true
                    }
                }
                else -> true
            }
        }
    }

}