package org.zstack.network.service.flat

import org.zstack.network.service.flat.APIGetL3NetworkIpStatisticReply

doc {
    title "GetL3NetworkIpStatistic"

    category "flat.dhcp"

    desc """在这里填写API描述"""

    rest {
        request {
			url "GET /v1/l3-networks/{l3NetworkUuid}/ip-statistic"

			header (Authorization: 'OAuth the-session-uuid')

            clz APIGetL3NetworkIpStatisticMsg.class

            desc """"""
            
			params {

				column {
					name "l3NetworkUuid"
					enclosedIn ""
					desc "三层网络UUID"
					location "url"
					type "String"
					optional false
					since "0.6"
					
				}
				column {
					name "resourceType"
					enclosedIn ""
					desc ""
					location "query"
					type "String"
					optional true
					since "0.6"
					values ("All","Vip","VM")
				}
				column {
					name "ip"
					enclosedIn ""
					desc ""
					location "query"
					type "String"
					optional true
					since "0.6"
					
				}
				column {
					name "sortBy"
					enclosedIn ""
					desc ""
					location "query"
					type "String"
					optional true
					since "0.6"
					values ("Ip","CreateDate")
				}
				column {
					name "sortDirection"
					enclosedIn ""
					desc ""
					location "query"
					type "String"
					optional true
					since "0.6"
					values ("asc","desc")
				}
				column {
					name "start"
					enclosedIn ""
					desc ""
					location "query"
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "limit"
					enclosedIn ""
					desc ""
					location "query"
					type "Integer"
					optional true
					since "0.6"
					
				}
				column {
					name "replyWithCount"
					enclosedIn ""
					desc ""
					location "query"
					type "boolean"
					optional true
					since "0.6"
					
				}
				column {
					name "systemTags"
					enclosedIn ""
					desc ""
					location "query"
					type "List"
					optional true
					since "0.6"
					
				}
				column {
					name "userTags"
					enclosedIn ""
					desc ""
					location "query"
					type "List"
					optional true
					since "0.6"
					
				}
			}
        }

        response {
            clz APIGetL3NetworkIpStatisticReply.class
        }
    }
}