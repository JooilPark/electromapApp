package com.sunday.electromapapp2.view.commons

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.sunday.electromapapp2.R

object BindingAdapter {
    @BindingAdapter("srcday", "dayindex")
    @JvmStatic
    fun loadOnOffDay(iv : ImageView ,boolean : Boolean  , dayindex : Int){
        when(dayindex){
            0->{ iv.setImageResource(if(boolean) R.drawable.on_mon else R.drawable.off_mon) }
            1->{ iv.setImageResource(if(boolean) R.drawable.on_tue else R.drawable.off_tue) }
            2->{ iv.setImageResource(if(boolean) R.drawable.on_wed else R.drawable.off_wed) }
            3->{ iv.setImageResource(if(boolean) R.drawable.on_thu else R.drawable.off_tue) }
            4->{ iv.setImageResource(if(boolean) R.drawable.on_fri else R.drawable.off_fri) }
            5->{ iv.setImageResource(if(boolean) R.drawable.on_sat else R.drawable.off_sat) }
            6->{ iv.setImageResource(if(boolean) R.drawable.on_sun else R.drawable.off_sun) }
        }

    }
    @BindingAdapter("isViewSelected")
    @JvmStatic
    fun isSelected(iv : View, isSelected : Boolean){
        iv.isSelected = isSelected
    }
    @BindingAdapter("isTextViewSelected")
    @JvmStatic
    fun isTextViewSelected(iv : TextView, isSelected : Boolean){
        iv.isSelected = isSelected
        iv.setTextColor(iv.context.getColor(if(isSelected)R.color.DetailTextColorWhite else R.color.DetailTextColorGray));
    }
}