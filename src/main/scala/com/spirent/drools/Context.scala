package com.spirent.drools

import javax.validation.{Validation, Validator}

/**
 * @author ysavi2
 * @since 14.12.2021
 */
object Context {
  var validator: Validator = Validation.buildDefaultValidatorFactory().getValidator
}
