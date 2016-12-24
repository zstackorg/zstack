doc {
    title "test"

    desc "this is a test"

    rest {
        request {
            url "POST /zones"
            header (
                    abc : "asdf",
                    xxx : 1
            )

            body {
                paramerters {
                }

                systemTags (1, 2, 3)
            }

            desc ""

            params {
                column {
                    name "name"
                    desc "zone name"
                    optional false
                    values ("xx","yyy","zzz")
                }
            }
        }
    }
}


