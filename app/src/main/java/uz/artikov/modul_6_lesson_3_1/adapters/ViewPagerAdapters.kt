package uz.artikov.modul_6_lesson_3_1.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.artikov.modul_6_lesson_3_1.FirstFragment
import uz.artikov.modul_6_lesson_3_1.secondFragment
import uz.mobiler.bootcamp.ThirdFragment

class ViewPagerAdapters(var list: ArrayList<String>, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return FirstFragment()

            }
            1 -> {

                return ThirdFragment()

            }
            2 -> {

                return secondFragment()

            }
        }

        return FirstFragment()

    }


    override fun getPageTitle(position: Int): CharSequence? {
        return list[position]
    }

}