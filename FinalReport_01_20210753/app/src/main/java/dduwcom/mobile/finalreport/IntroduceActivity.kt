package dduwcom.mobile.finalreport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dduwcom.mobile.finalreport.databinding.ActivityAddBinding
import dduwcom.mobile.finalreport.databinding.ActivityIntroduceBinding

class IntroduceActivity: AppCompatActivity() {
    val TAG = "IntroduceActivity"

    lateinit var introduceBinding: ActivityIntroduceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        introduceBinding = ActivityIntroduceBinding.inflate(layoutInflater)
        setContentView(introduceBinding.root)

        introduceBinding.btnIntroduce.setOnClickListener {
            finish()
        }
    }
}