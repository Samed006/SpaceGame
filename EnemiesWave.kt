package com.example.project

class EnemiesWave( x1: Float,  y1: Float, diametre: Float, canonView: CanonView) : Enemies( x1,  y1,  diametre , canonView) {
    val vagueUne = arrayListOf<Enemies>(Enemies(x1,y1,diametre,canonView),Enemies(x1,y1,diametre,canonView),Enemies(30f,30f,80f,canonView),Enemies(200f,200f,80f,canonView),Enemies(400f,400f,80f,canonView))
    val vagueUneEn = 5
    var enemiesKill = 0

    fun resetEnemies(){
        for (b in vagueUne){
            b.enemiesHit = false
            enemiesKill = 0
            b.setRect()
        }
    }
    fun sendMessage(){
        for(j in vagueUne){
            if(j.enemiesHit) {
                enemiesKill = enemiesKill + 1
            }}
        if(enemiesKill != vagueUneEn){
            enemiesKill = 0
        }else{
            canonView.gameOver()
        }
    }}
