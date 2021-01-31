package bean

import java.sql.Date

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 19:04
 */
case class CovidData(
                      // date
                      date: Date,
                      // province
                      name_jp: String,
                      // patients
                      npatients: Int,

                    )
