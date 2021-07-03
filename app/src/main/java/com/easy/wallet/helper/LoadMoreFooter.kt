package com.easy.wallet.helper

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import com.easy.wallet.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LoadMoreFooter(context: Context) : FrameLayout(context) {
    init {
        View.inflate(context, R.layout.loading_footer, this)
    }
}
