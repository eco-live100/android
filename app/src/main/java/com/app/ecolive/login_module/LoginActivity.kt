package com.app.ecolive.login_module


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.app.ecolive.R
import com.app.ecolive.common_screen.UserHomePageNavigationActivity
import com.app.ecolive.databinding.ActivityLoginBinding
import com.app.ecolive.service.Status
import com.app.ecolive.shop_owner.ShopOwnerHomePageNavigationActivity
import com.app.ecolive.utils.*
import com.app.ecolive.viewmodel.CommonViewModel

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.TwitterAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offercity.base.BaseActivity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import java.util.*
import com.facebook.AccessToken
import twitter4j.User
import twitter4j.api.UsersResources
import kotlin.concurrent.thread
import twitter4j.auth.AccessToken as twitter4j

class LoginActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {
    lateinit var binding: ActivityLoginBinding
    var userType = ""
    private val progressDialog = CustomProgressDialog()
    private lateinit var auth: FirebaseAuth
    private var callbackManager: CallbackManager? = null

    //google
    private var firebaseAuth: FirebaseAuth? = null
    private var mAuthListener: AuthStateListener? = null

    private val RC_SIGN_IN = 1
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //This code must be entering before the setContentView to make the twitter login work...
//        val mTwitterAuthConfig = TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret))
//        val twitterConfig = TwitterConfig.Builder(this).twitterAuthConfig(mTwitterAuthConfig).build()
//        Twitter.initialize(twitterConfig)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = Firebase.auth
        getExtraDataIntent()
        initView()
        //val currentUser = auth.currentUser
        firebaseAuth = FirebaseAuth.getInstance()
        //gooogle
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        //twitter
        /* binding.twitterLoginOriginal.setCallback(object : Callback<TwitterSession?>() {


             override fun failure(exception: TwitterException?) {
                 Toast.makeText(this@LoginActivity, "Login failed. No Twitter app found on your phone", Toast.LENGTH_LONG).show()



             }

             override fun success(result: Result<TwitterSession?>?) {
                 Toast.makeText(this@LoginActivity, "Signed in to twitter successful1", Toast.LENGTH_LONG).show()
                 result?.data?.let { signInToFirebaseWithTwitterSession(it) }



             }
         })*/
    }


    /* private fun signInToFirebaseWithTwitterSession(session: TwitterSession) {
         val credential = TwitterAuthProvider.getCredential(session.authToken.token, session.authToken.secret)
         Log.d("ok", "signInToFirebaseWithTwitterSession: "+credential)
         val session = TwitterCore.getInstance().sessionManager.activeSession
         val authToken = session.authToken
         val token = authToken.token
         val secret = authToken.secret
        var vv1= session.userName
        var vv2= session.userId


         val authClient = TwitterAuthClient()
         authClient.requestEmail(session, object : Callback<String?>() {
             override fun success(result: Result<String?>?) {
                 // Do something with the result, which provides the email address
                 Log.d("TAG", "success: "+result?.data)
             }

             override fun failure(exception: TwitterException?) {
                 // Do something on failure
                 Log.d("TAG", "failure: "+exception)
             }
         })

       //  getTwitterUserProfile(session);
       *//*  auth.signInWithCredential(credential)
            .addOnCompleteListener(this
            ) { task ->
                Toast.makeText(
                    this@LoginActivity,
                    "Signed in firebase twitter successful2",
                    Toast.LENGTH_LONG
                ).show()
                if (!task.isSuccessful()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Auth firebase twitter failed2",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }*//*
    }
    private fun getTwitterUserProfile(session: TwitterSession) {
        val accountService = TwitterApiClient(session).accountService
        val callback  = accountService.verifyCredentials(true, true, true)
        callback.clone().enqueue(object : Callback<User?>() {
            override fun failure(exception: TwitterException?) {
                Log.d("TAG", "failure: "+exception)
            }
            override fun success(result: Result<User?>?) {
                Log.d("NAME ", result?.data!!.name)
                Log.d("EMAIL", result?.data!!.email)
                Log.d("PICTURE ", result.data!!.profileImageUrl)
            }
        })
    }*/

    override fun onClick(v: View?) {
        super.onClick(v)
        if (MyApp.isConnectingToInternet(THIS!!)) {
            if (v == binding.loginButton) {
                if (isValidInput()) {
                    loginApiCall()
                }
            } else if (v == binding.loginForgot) {
                startActivity(Intent(THIS!!, ForgotPwdActivity::class.java))
            } else if (v == binding.loginFbBtn) {
               // fbLogin()
            } else if (v == binding.loginGoogleBtn) {
                googleLogin()
            } else if (v == binding.loginTwitterBtn) {
                  getRequestToken()
                //  binding.twitterLoginOriginal.performClick()
            }
        } else {
            MyApp.popErrorMsg("", resources.getString(R.string.No_Internet), THIS!!)
        }
    }

    lateinit var twitter: Twitter
    lateinit var twitterDialog: Dialog
    var accToken: AccessToken? = null
    private fun getRequestToken() {
        GlobalScope.launch(Dispatchers.Default) {
            val builder = ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(resources.getString(R.string.twitter_consumer_key))
                .setOAuthConsumerSecret(resources.getString(R.string.twitter_consumer_secret))
                .setIncludeEmailEnabled(true)
            val config = builder.build()
            val factory = TwitterFactory(config)
            twitter = factory.instance
            try {
                val requestToken = twitter.oAuthRequestToken
                withContext(Dispatchers.Main) {
                    setupTwitterWebviewDialog(requestToken.authorizationURL)
                }
            } catch (e: IllegalStateException) {
                Log.e("okt", e.toString())
            }
        }
    }

    // Show twitter login page in a dialog
    @SuppressLint("SetJavaScriptEnabled")
    fun setupTwitterWebviewDialog(url: String) {
        twitterDialog = Dialog(this)
        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = TwitterWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        twitterDialog.setContentView(webView)
        twitterDialog.show()
    }

    // A client to know about WebView navigations
    // For API 21 and above
    @Suppress("OverridingDeprecatedMember")
    inner class TwitterWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            if (request?.url.toString().startsWith(resources.getString(R.string.twitter_callback_url))) {
                Log.d("ok Authorization URL: ", request?.url.toString())
                handleUrlTwitter(request?.url.toString())
                // Close the dialog after getting the oauth_verifier
                if (request?.url.toString().contains(resources.getString(R.string.twitter_callback_url))) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        // For API 19 and below
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(resources.getString(R.string.twitter_callback_url))) {
                Log.d("Authorization URL: ", url)
                handleUrlTwitter(url)
                // Close the dialog after getting the oauth_verifier
                if (url.contains(resources.getString(R.string.twitter_callback_url))) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        // Get the oauth_verifier
        private fun handleUrlTwitter(url: String) {
            val uri = Uri.parse(url)//https://ecolive.ezxdemo.com/server/api1/twitter-callback-response?oauth_token=Mgcb3wAAAAABdOe0AAABgWG78cw&oauth_verifier=Fzhy9Kwccf21B1RPp84b0i8S95CmfGOL
              val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""
              GlobalScope.launch(Dispatchers.Main) {
           // accToken     =  withContext(Dispatchers.IO) { twitter.getOAuthAccessToken(oauthVerifier) }
             // getUserProfile()
                }
            lifecycleScope.launch(Dispatchers.Main) {
                getUserProfile()
            }
        }
    }

    lateinit var usr: User

    fun getUserProfile() {
        // val usr = withContext(Dispatchers.IO) { twitter.verifyCredentials() }
        lifecycleScope.launch(Dispatchers.IO) {
            usr = twitter.verifyCredentials()
            //Twitter Id
            val twitterId = usr.id.toString()
            Log.d("Twitter Id: ", twitterId)

            //Twitter Handle
            val twitterHandle = usr.screenName
            Log.d("Twitter Handle: ", twitterHandle)

            //Twitter Name
            val twitterName = usr.name
            Log.d("Twitter Name: ", twitterName)

            //Twitter Email
            val twitterEmail = usr.email
            Log.d(
                "Twitter Email: ",
                twitterEmail
                    ?: "'Request email address from users' on the Twitter dashboard is disabled"
            )

            // Twitter Profile Pic URL
            val twitterProfilePic = usr.profileImageURLHttps.replace("_normal", "")
            Log.d("Twitter Profile URL: ", twitterProfilePic)

            // Twitter Access Token
            Log.d("Twitter Access Token", accToken?.token ?: "")
        }
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun fbLogin() {
         // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
          LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_friends"));
       // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                    Log.d("okfb", "onSuccess: ")
                    handleFacebookAccessToken(loginResult?.accessToken)
                    LoginManager.getInstance().logOut();
                    FirebaseAuth.getInstance().signOut()
                }

                override fun onCancel() {
                    Log.d("okfb", "onCancel: ")
                }

                override fun onError(exception: FacebookException) {
                    Log.d("okfb", "onError: " + exception.message)
                }
            })

    }

    private fun handleFacebookAccessToken(token: AccessToken?) {
        Log.d("ok", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token!!.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("ok", "signInWithCredential:success")
                    val user = auth.currentUser
                    var vv = user?.email
                    var vv2 = user?.displayName
                    var vv3 = user?.photoUrl
                    var vv4 = user?.phoneNumber
                    var vv5 = user?.uid
                    // Toast.makeText(baseContext, "FB success", Toast.LENGTH_SHORT).show()
                    Log.d("ok", "handleFacebookAccessToken: " + user)
                    socialLoginAPICall("facebook", user?.uid,  user?.displayName, user?.email)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ok", "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()

                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        // binding.twitterLoginOriginal.onActivityResult(requestCode, resultCode, data);
        if (requestCode === RC_SIGN_IN) {
//            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
//            handleSignInResult(result!!)
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("ok", "firebaseAuthWithGoogle:" + account.id)
                Log.d("ok", "firebaseAuthWithGoogle:" + account.idToken!!)
                var vv1 = account.id
                var vv3 = account.email
                var vv4 = account.displayName
                var vv5 = account.photoUrl
                // firebaseAuthWithGoogle(account.idToken!!)
               socialLoginAPICall("google", account.id,account.email,account.displayName)
                googleSignInClient.signOut()//logout from google
                FirebaseAuth.getInstance().signOut()
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("ok", "Google sign in failed", e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //  auth.removeAuthStateListener(mAuthListener)
    }

    private fun loginApiCall() {
        progressDialog.show(THIS!!)
        var loginViewModel = CommonViewModel(THIS!!)
        var json = JSONObject()
        json.put("emailMobile", binding.loginPhNo.text.toString())
        json.put("password", binding.loginPwd.text.toString())
        loginViewModel.userLogin(json).observe(THIS!!) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let {
                        var vv = it.data
                        PreferenceKeeper.instance.bearerTokenSave = it.data.accessToken
                      //  if (binding.loginIsRemeberMe.isChecked) {
                            PreferenceKeeper.instance.isUserLogin = true
                       // }
                        PreferenceKeeper.instance.loginResponse = it.data
                        startActivity(Intent(THIS, UserHomePageNavigationActivity::class.java))
                        finish()

                    }

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    var vv = it.message
                  var msg = JSONObject(it.message)
                    MyApp.popErrorMsg("", "" + msg.getString("msg"), THIS!!)
                   // MyApp.popErrorMsg("", "" + vv, THIS!!)
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        if (binding.loginPhNo.text.toString().isBlank()) {
            MyApp.popErrorMsg("", "Please enter email", THIS!!)
            return false
        } else if (binding.loginPwd.text.toString().isBlank()) {
            MyApp.popErrorMsg("", "Please enter password", THIS!!)
            return false
        }
        return true
    }

    private fun initView() {
        statusBarColor()
        setTouchNClick(binding.loginButton)
        setTouchNClick(binding.loginForgot)
        setTouchNClick(binding.loginFbBtn)
        setTouchNClick(binding.loginGoogleBtn)
        setTouchNClick(binding.loginTwitterBtn)

        binding.signupLabel.setOnClickListener {
            startActivity(Intent(this@LoginActivity, UserSignupActivity::class.java))
            finish()
            /*  when (userType) {
                 AppConstant.USER_TYPE.TYPE_USER -> {
                     startActivity(Intent(this@LoginActivity, UserSignupActivity::class.java))
                 }
                 AppConstant.USER_TYPE.TYPE_SHOP -> {
                     startActivity(
                         Intent(
                             this@LoginActivity,
                             UserSignupActivity::class.java
                         )
                     )//TYPE_SHOP  do later screen pending
                 }
                 AppConstant.USER_TYPE.TYPE_RIDER -> {
                     startActivity(
                         Intent(
                             this@LoginActivity,
                             UserSignupActivity::class.java
                         )
                     )//TYPE_RIDER  do later screen pending
                 }
             }

             startActivity(Intent(this@LoginActivity, UserTypeOptionActivity::class.java).
              putExtra(AppConstant.INTENT_EXTRAS.INTENT_TYPE, AppConstant.INTENT_VALUE.INTENT_TYPE_SIGNUP))*/
        }


        binding.loginSkipNow.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    UserHomePageNavigationActivity::class.java
                )
            )
           /* when (userType) {
                AppConstant.USER_TYPE.TYPE_USER -> {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            UserHomePageNavigationActivity::class.java
                        )
                    )

                }
                AppConstant.USER_TYPE.TYPE_SHOP -> {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            ShopOwnerHomePageNavigationActivity::class.java
                        )
                    )
                }
                AppConstant.USER_TYPE.TYPE_RIDER -> {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            UserHomePageNavigationActivity::class.java
                        )
                    )

                }
            }*/
            finish()
        }

    }

    private fun statusBarColor() {
        Utils.changeStatusColor(THIS!!, R.color.color_F5F5F5)
        Utils.changeStatusTextColor(THIS!!)
    }

    private fun getExtraDataIntent() {
        when {
            intent.getStringExtra(AppConstant.INTENT_EXTRAS.USER_TYPE) != null -> {
                userType = intent.getStringExtra(AppConstant.INTENT_EXTRAS.USER_TYPE)!!
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun socialLoginAPICall(socialLoginType: String, socialLoginId: String?, socialEmail: String?, socialLoginName: String?) {
        progressDialog.show(THIS!!)
        var loginViewModel = CommonViewModel(THIS!!)
        var json = JSONObject()
        json.put("socialLoginType", socialLoginType)
        json.put("socialLoginId", socialLoginId)

        loginViewModel.userSocialLogin(json).observe(THIS!!) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    it.data?.let {

                        PreferenceKeeper.instance.bearerTokenSave = it.data.accessToken
                        // if (binding.loginIsRemeberMe.isChecked) {
                        PreferenceKeeper.instance.isUserLogin = true
                        //  }
                        PreferenceKeeper.instance.loginResponse = it.data
                        startActivity(Intent(THIS, UserHomePageNavigationActivity::class.java))
                        finish()

                    }

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    progressDialog.dialog.dismiss()
                    if (it.message == "User with this social id does not exists") {
                        DialogCustmYesNo.getInstance().createDialog(
                            THIS!!,
                            "User does not exists",
                            "Do you want to sign up now?",
                            "Yes",
                            "No",
                            object : DialogCustmYesNo.Dialogclick {
                                override fun onYES() {
                                    startActivity(
                                        Intent(THIS!!, UserSignupActivity::class.java)
                                            .putExtra(AppConstant.SOCIAL_LOGIN_TYPE, socialLoginType)
                                            .putExtra(AppConstant.SOCIAL_LOGIN_ID, socialLoginId)
                                            .putExtra(AppConstant.SOCIAL_LOGIN_NAME, socialLoginName)
                                            .putExtra(AppConstant.SOCIAL_LOGIN_EMAIL, socialEmail)
                                    )
                                    finish()
                                }

                                override fun onNO() {

                                }

                            })
                    }

                }
            }
        }
    }

}