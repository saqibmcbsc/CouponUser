package com.couponusers.couponuser.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.couponusers.couponuser.Adapter.FragmentPageAdapter_Notification
import com.example.couponuser.databinding.FragmentNotificationBinding
import com.google.android.material.tabs.TabLayout


class Notification_Fragment : Fragment() {

    lateinit var binding: FragmentNotificationBinding
    lateinit var adapter: FragmentPageAdapter_Notification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(view == null) {
            binding = FragmentNotificationBinding.inflate(inflater, container, false)

            adapter = activity?.let {
                FragmentPageAdapter_Notification(
                    it.supportFragmentManager,
                    lifecycle
                )
            }!!
            binding.viewPager2Notification.adapter = adapter

            binding.imgNotificationClose.setOnClickListener(View.OnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            })

            binding.tabLayoutNotification.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        binding.viewPager2Notification.currentItem = tab.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }


            })
            binding.viewPager2Notification.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tabLayoutNotification.selectTab(
                        binding.tabLayoutNotification.getTabAt(
                            position
                        )
                    )
                }
            })
        }
        return binding.root
    }


}