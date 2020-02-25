package com.example.data.contentful

import com.contentful.vault.ContentType
import com.contentful.vault.Field
import com.contentful.vault.Resource

@ContentType("termsAndConditions")
class TermsAndConditionsResponse : Resource() {

    @JvmField
    @Field
    var id: String? = null

    @JvmField
    @Field
    var title: String? = null

    @JvmField
    @Field
    var content: String? = null
}