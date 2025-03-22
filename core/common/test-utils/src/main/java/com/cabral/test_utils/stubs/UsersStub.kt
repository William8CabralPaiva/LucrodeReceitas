package com.cabral.test_utils.stubs

import com.cabral.core.common.domain.model.User

fun userStub(): User {
    return User(
        email = "test_email",
        name = "test_name",
        password = "test_password",
        key = "test_key"
    )
}