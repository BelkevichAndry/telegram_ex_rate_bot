(defproject myproject "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ             "1.1.0"]
                 [morse               "0.2.4"]
		 [http-kit "2.2.0"]
		 [ring/ring-json "0.4.0"]
		 [org.clojure/data.json "0.2.6"]
                 [hickory "0.7.1"]]

  :plugins [[lein-environ "1.1.0"]]

  :main ^:skip-aot myproject.core
  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}})