package com.jzoft.ygohelper

import android.app.Application
import android.content.Context
import com.focaltec.gastosdeviaje.gastosdeviajeapp.rest.RetrofitClient
import com.github.salomonbrys.kodein.*
import com.jzoft.ygohelper.biz.ProxyCardLocator
import com.jzoft.ygohelper.biz.ProxyCardPrinter
import com.jzoft.ygohelper.biz.impl.*
import com.jzoft.ygohelper.rest.ApiService
import com.jzoft.ygohelper.utils.impl.CallerCache
import com.jzoft.ygohelper.utils.impl.CallerFactoryHttp
import com.jzoft.ygohelper.utils.impl.CallerHttpConnection
import com.jzoft.ygohelper.utils.impl.ImageOptimizerDisplay

/**
 * Created by jjimenez on 3/08/17.
 */
class YgoHelperApp() : Application(), KodeinAware {
    private val baseUrl = ""

    override val kodein by Kodein.lazy {
        //        val appProperties = AppProperties(applicationContext)
//        bind<AppProperties>() with singleton { appProperties }
//        url = localProperties(assets.open("application.properties")).getProperty("url.api")!!
//        bind<DataAccess>() with provider { DataAccessDbFlow(instance()) }
//        bind<LoginClient>() with provider { LoginClient(url + "oauth/", secret) }
//        bind<RetrofitClient>() with provider { RetrofitClient(url) }
//        bind<ServerOperations>("client") with singleton { ServerOperationsRetrofit(instance(), instance(), instance()) }
//        bind<ServerOperations>() with provider { ServerOperationSaver(instance("client"), instance()) }
        bind<ProxyCardPrinter>() with factory { ctx: Context -> ProxyCardWebView(ctx) }

        bind<ProxyCardLocator>() with provider {
            ProxyCardLocatorLinked.buildPatchLocatorLinked(
                    ProxyCardLocatorWordToUrlWikia(), ProxyCardLocatorUrlWikiaToImageUrl(CallerFactoryHttp()),
                    ProxyCardLocatorUrlToImage(CallerCache(ImageOptimizerDisplay(CallerHttpConnection()))))!!
        }
        bind<RetrofitClient>() with singleton { RetrofitClient() }
        bind<ApiService>("free") with singleton { instance<RetrofitClient>().apiService() }
        bind<ApiService>() with provider { instance<RetrofitClient>().apiService(baseUrl) }
        ctx = applicationContext
    }

//    private fun localProperties(open: InputStream): Properties {
//        val props = Properties()
//        props.load(open)
//        return props
//    }

    override fun onCreate() {
        super.onCreate()
//        FlowManager.init(this)
    }

    companion object {

        private lateinit var ctx: Context
        fun getString(id: Int): String = ctx.getString(id)
    }
}