package uz.artikov.modul_6_lesson_3_1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import uz.artikov.modul_6_lesson_3_1.databinding.ItemSpinnerBinding

class SpinnerAdapter(var list: ArrayList<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): String {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(p0: Int, convertView: View?, parent: ViewGroup?): View {
        var basicViewHolder:BasicViewHolder
        if (convertView==null){
            val binding =
                ItemSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
                basicViewHolder=BasicViewHolder(binding)
        }else{
            basicViewHolder=BasicViewHolder(ItemSpinnerBinding.bind(convertView))
        }
        basicViewHolder.itemSpinnerBinding.textSpinner.text=list[p0]
        return basicViewHolder.itemView
    }

    inner class BasicViewHolder{
        var itemView:View
        var itemSpinnerBinding:ItemSpinnerBinding

        constructor(itemSpinnerBinding: ItemSpinnerBinding) {
            itemView=itemSpinnerBinding.root
            this.itemSpinnerBinding = itemSpinnerBinding
        }
    }
}