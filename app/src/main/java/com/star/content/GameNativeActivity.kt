package com.star.content

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.star.content.adapter.GameAdapter
import com.star.content.bean.Game
import com.starmedia.tinysdk.StarMedia
import kotlinx.android.synthetic.main.activity_game_native.*

class GameNativeActivity: BaseActivity() {

    private val games = ArrayList<Game>().apply {
        add(Game(10001, R.drawable.icon_game_10001, "开心钓钓乐"))
        add(Game(10002, R.drawable.icon_game_10002, "连连看"))
        add(Game(10003, R.drawable.icon_game_10003, "六角消除"))
        add(Game(10004, R.drawable.icon_game_10004, "开心小农场"))
        add(Game(10005, R.drawable.icon_game_10005, "成语大官人"))
        add(Game(10006, R.drawable.icon_game_10006, "消灭僵尸（庄园保卫战）"))
        add(Game(10007, R.drawable.icon_game_10007, "开心斗地主"))
        add(Game(10008, R.drawable.icon_game_10008, "水果杀手"))
        add(Game(10009, R.drawable.icon_game_10009, "左右消滑"))
        add(Game(10010, R.drawable.icon_game_10010, "养猪大亨"))
        add(Game(10011, R.drawable.icon_game_10011, "快来当岛主"))
        add(Game(10012, R.drawable.icon_game_10012, "扫雷大作战"))
        add(Game(10013, R.drawable.icon_game_10013, "娘娘养成记"))
        add(Game(10014, R.drawable.icon_game_10014, "全明星足球"))
        add(Game(10015, R.drawable.icon_game_10015, "水果忍者"))
        add(Game(10016, R.drawable.icon_game_10016, "滚动的天空2"))
        add(Game(10017, R.drawable.icon_game_10017, "经典打砖块（砖块消消消）"))
        add(Game(10018, R.drawable.icon_game_10018, "疯狂消星星"))
        add(Game(10019, R.drawable.icon_game_10019, "开心六角消除"))
        add(Game(10020, R.drawable.icon_game_10020, "欢乐碰碰球（吃鸡碰碰碰）"))
        add(Game(10021, R.drawable.icon_game_10021, "欢乐猜字"))
        add(Game(10022, R.drawable.icon_game_10022, "黄金矿工"))
        add(Game(10023, R.drawable.icon_game_10023, "打球球"))
        add(Game(10024, R.drawable.icon_game_10024, "泡泡龙"))
        add(Game(10025, R.drawable.icon_game_10025, "跳跃忍者"))
        add(Game(10026, R.drawable.icon_game_10026, "我是神枪手"))
        add(Game(10027, R.drawable.icon_game_10027, "塔防三国"))
        add(Game(10028, R.drawable.icon_game_10028, "热血篮球"))
        add(Game(10029, R.drawable.icon_game_10029, "航海传奇"))
        add(Game(10030, R.drawable.icon_game_10030, "街头篮球"))
        add(Game(10031, R.drawable.icon_game_10031, "五子棋"))
        add(Game(10032, R.drawable.icon_game_10032, "乒乓"))
        add(Game(10033, R.drawable.icon_game_10033, "跳舞的线"))
        add(Game(10034, R.drawable.icon_game_10034, "幸福餐厅（幸福厨房）"))
        add(Game(10035, R.drawable.icon_game_10035, "斗兽棋"))
        add(Game(10036, R.drawable.icon_game_10036, "快乐拼拼"))
    }

    private val gameAdapter: GameAdapter by lazy {
        GameAdapter(games, true) { game ->
            StarMedia.openStarGame(this, "{\"game_id\":${game.code}, \"menu\":false}") {
                if (!it) {
                    Toast.makeText(this, "开发游戏失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_native)

        rv_game_native_group.layoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        rv_game_native_group.adapter = gameAdapter
    }
}