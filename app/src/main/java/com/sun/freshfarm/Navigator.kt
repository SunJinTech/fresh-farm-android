package com.sun.freshfarm

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.sun.freshfarm.confirmcase.CaseActivity
import com.sun.freshfarm.confirmcase.CaseDetailActivity
import com.sun.freshfarm.disease.detail.DiseaseDetailActivity
import com.sun.freshfarm.report.submit.ReportSubmitActivity
import com.sun.freshfarm.home.HomeActivity
import com.sun.freshfarm.livestock.detail.LivestockDetailActivity
import com.sun.freshfarm.livestock.select.LivestockSelectActivity
import com.sun.freshfarm.map.MapCaseActivity
import com.sun.freshfarm.report.ReportActivity
import com.sun.freshfarm.report.ReportEditActivity
import com.sun.freshfarm.report.detail.ReportDetailActivity
import com.sun.freshfarm.user.login.LoginActivity
import com.sun.freshfarm.user.profile.edit.ProfileEditActivity
import com.sun.freshfarm.user.signup.CreateAccountActivity
import com.sun.freshfarm.user.signup.EmailVerification
import com.sun.freshfarm.user.signup.SignUpEmail

class Navigator(val context: Context) {

    fun doInitial() {
        val intent =
            Intent(context, InitialActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }

    fun goHomePage() {
        val intent =
            Intent(context, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }

    fun goLoginPage() {
        val intent =
            Intent(context, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }

    fun goSignUpPage() {
        context.startActivity(Intent(context, SignUpEmail::class.java))
        (context as Activity).finish()
    }

    fun goEmailVerificationPage() {
        context.startActivity(Intent(context, EmailVerification::class.java))
        (context as Activity).finish()
    }

    fun goCreatingAccountPage(name: String, email: String, password: String) {
        val intent = Intent(context, CreateAccountActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("email", email)
        intent.putExtra("password", password)

        context.startActivity(intent)
        (context as Activity).finish()
    }

    fun goProfileEdit(uid: String) {
        val intent = Intent(context, ProfileEditActivity::class.java).putExtra("uid", uid)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }

    fun startProfileEdit(uid: String) {
        val intent = Intent(context, ProfileEditActivity::class.java).putExtra("uid", uid)
        context.startActivity(intent)
    }

    fun startLivestockSelect() {
        val intent = Intent(context, LivestockSelectActivity::class.java)
        context.startActivity(intent)
    }

    fun startLivestockDetail(kind: String) {
        val intent = Intent(context, LivestockDetailActivity::class.java).putExtra("kind", kind)
        context.startActivity(intent)
    }


    fun startCaseDetail(id: String) {
        val intent = Intent(context, CaseDetailActivity::class.java).putExtra("id", id)
        context.startActivity(intent)
    }

    fun startCaseList() {
        val intent = Intent(context, CaseActivity::class.java)
        context.startActivity(intent)
    }

    fun startReportList() {
        val intent = Intent(context, ReportActivity::class.java)
        context.startActivity(intent)
    }

    fun startReportDetail(reportId: String) {
        val intent = Intent(
            context,
            ReportDetailActivity::class.java
        ).putExtra("id", reportId)
        context.startActivity(intent)
    }

    fun startReportSubmit(code: String, kind: String) {
        val intent = Intent(
            context,
            ReportSubmitActivity::class.java
        )
            .putExtra("code", code)
            .putExtra("kind", kind)
        context.startActivity(intent)
    }

    fun startReportEdit(id: String) {
        val intent = Intent(
            context,
            ReportEditActivity::class.java
        ).putExtra("id", id)
        context.startActivity(intent)
    }

    fun startDiseaseDetail(code: String) {
        val intent = Intent(
            context,
            DiseaseDetailActivity::class.java
        ).putExtra("code", code)
        context.startActivity(intent)
    }

    fun startMapCase() {
        val intent = Intent(context, MapCaseActivity::class.java)
        context.startActivity(intent)
    }

}