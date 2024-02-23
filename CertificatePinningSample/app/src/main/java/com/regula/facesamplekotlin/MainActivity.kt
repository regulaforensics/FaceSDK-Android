package com.regula.facesamplekotlin

import android.net.http.X509TrustManagerExtensions
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.regula.common.http.HttpRequestBuilder
import com.regula.facesamplekotlin.databinding.ActivityMainBinding
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.exception.InitException
import com.regula.facesdk.listener.NetworkInterceptorListener
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.Arrays
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLException
import javax.net.ssl.SSLPeerUnverifiedException
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class MainActivity : AppCompatActivity(), NetworkInterceptorListener {

    val VALID_PINS = setOf("/5RKFaPkCjAzvsEZHOlYqncYADaLIG5VfTmhsBbkaBk=")

    @Transient
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FaceSDK.Instance().initialize(this) { status: Boolean, e: InitException? ->
            if (!status) {
                Toast.makeText(
                    this@MainActivity,
                    "Init finished with error: " + if (e != null) e.message else "",
                    Toast.LENGTH_LONG
                ).show()
                return@initialize
            }
            FaceSDK.Instance().setLogs(true)
            FaceSDK.Instance().setNetworkInterceptorListener(this@MainActivity)
            (Handler(Looper.getMainLooper())).post {
                binding.startBtn.isEnabled = true
            }
            Log.d("MainActivity", "FaceSDK init completed successfully")
        }

        binding.startBtn.setOnClickListener {
            FaceSDK.Instance().startLiveness(this) {
                if (it.exception != null)
                    Toast.makeText(this, "Error: " + it.exception!!.message, Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Liveness status: " + it.liveness.name, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPrepareRequest(connection: HttpRequestBuilder?) {
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
// Find first X509TrustManager in the TrustManagerFactory
// Find first X509TrustManager in the TrustManagerFactory
        var x509TrustManager: X509TrustManager? = null
        for (trustManager in trustManagerFactory.trustManagers) {
            if (trustManager is X509TrustManager) {
                x509TrustManager = trustManager
                break
            }
        }
        val trustManagerExt = X509TrustManagerExtensions(x509TrustManager)
        connection?.connection?.let {
            println("validatePinning")
            try {
                connection.connection.connect()
            } catch (e: Exception) {
                println("Error connect: " + e)
            }
            validatePinning(trustManagerExt, connection.connection as HttpsURLConnection, VALID_PINS)
        }

    }

    @Throws(SSLException::class)
    private fun validatePinning(
        trustManagerExt: X509TrustManagerExtensions,
        conn: HttpsURLConnection, validPins: Set<String>
    ) {
        var certChainMsg = ""
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val trustedChain: List<X509Certificate> = trustedChain(trustManagerExt, conn)
            println("trustedChain count: " + trustedChain.size)
            for (cert: X509Certificate in trustedChain) {
                val publicKey: ByteArray = cert.publicKey.encoded
                md.update(publicKey, 0, publicKey.size)
                val pin: String = Base64.encodeToString(
                    md.digest(),
                    Base64.NO_WRAP
                )
                certChainMsg += ("    sha256/" + pin + " : " +
                        cert.subjectDN.toString()) + "\n"
                if (validPins.contains(pin)) {
                    return
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            throw SSLException(e)
        }
        throw SSLPeerUnverifiedException(
            "Certificate pinning " +
                    "failure\n  Peer certificate chain:\n" + certChainMsg
        )
    }

    @Throws(SSLException::class)
    private fun trustedChain(
        trustManagerExt: X509TrustManagerExtensions,
        conn: HttpsURLConnection
    ): List<X509Certificate> {
        val serverCerts: Array<Certificate> = conn.serverCertificates
        val untrustedCerts: Array<X509Certificate> = Arrays.copyOf(
            serverCerts,
            serverCerts.size, Array<X509Certificate>::class.java
        )
        val host = conn.url.host
        try {
            return trustManagerExt.checkServerTrusted(
                untrustedCerts,
                "RSA", host
            )
        } catch (e: CertificateException) {
            throw SSLException(e)
        }
    }
}