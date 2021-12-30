package uz.artikov.modul_6_lesson_3_1.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import uz.artikov.modul_6_lesson_3_1.databinding.ItemRvBinding
import uz.artikov.modul_6_lesson_3_1.models.Bootcamp


class RvAdapters(var list: ArrayList<Bootcamp>) : RecyclerView.Adapter<RvAdapters.Vh>() {

    var myImgItemOnClickListener: MyImgItemOnClickListener? = null
    private val TAG = "WorldFragment"

    fun setMyImgOnClick(myImgItemOnClickListener: MyImgItemOnClickListener) {
        this.myImgItemOnClickListener = myImgItemOnClickListener
    }


    var myItemOnClickListener: MyItemOnClickListener? = null
    fun setMyItemOnClick(myItemOnClickListener: MyItemOnClickListener) {
        this.myItemOnClickListener = myItemOnClickListener
    }


    inner class Vh(var itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        @SuppressLint("WrongConstant")
        fun onBind(bootcamp: Bootcamp, position: Int) {
            itemRvBinding.tv1.text = list[position].name
            itemRvBinding.tv2.text = list[position].description

            itemRvBinding.img.setOnClickListener {
                myImgItemOnClickListener?.onMyItemClick(bootcamp, position, itemRvBinding.img)
            }



            itemRvBinding.root.setOnClickListener {
                myItemOnClickListener?.onItemClick(bootcamp, position)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MyImgItemOnClickListener {
        fun onMyItemClick(bootcamp: Bootcamp, position: Int, imageView: ImageView)
    }

    interface MyItemOnClickListener {
        fun onItemClick(bootcamp: Bootcamp, position: Int)
    }
}