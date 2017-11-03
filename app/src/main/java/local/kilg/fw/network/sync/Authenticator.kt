package local.kilg.fw.network.sync

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.content.Context
import android.os.Bundle

/**
 * Created by kilg on 17.10.17.
 */
class Authenticator(context: Context): AbstractAccountAuthenticator(context) {
    override fun getAuthTokenLabel(authTokenType: String?): String {
        return "kilg"
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?,
                                    account: Account?,
                                    options: Bundle?): Bundle? = null

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        throw UnsupportedOperationException()
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
            throw UnsupportedOperationException()
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        throw UnsupportedOperationException()
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle {
        throw UnsupportedOperationException()
    }

    override fun addAccount(response: AccountAuthenticatorResponse?,
                            accountType: String?,
                            authTokenType: String?,
                            requiredFeatures: Array<out String>?,
                            options: Bundle?): Bundle? = null
}