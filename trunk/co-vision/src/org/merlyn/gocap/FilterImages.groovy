package org.merlyn.gocap

class FilterImages {
	static void main(args) {
	    def frames = []

	    def lastTimestamp = 0
	    def last = null

	    new File('game11/squewed').listFiles().each { it.delete() }

	    def index = 0
	    def directory = 'C:\\Program Files\\Dorgem\\captures11'

		new File(directory).list().each { jpg ->
		    def m = jpg =~ /[a-z]*(\d+)\.jpg/
	        if (m.getCount() == 1) {
	            def timestamp = Integer.parseInt(m[0][1])
	            if (timestamp - lastTimestamp >= 2 && last != null) {
	                frames << last
	                def i = "00000"+(index++)
	                i = i.substring(i.length() - 5)
	                println "$last --> $i (diff: ${timestamp - lastTimestamp})"
	                new File("game11/squewed/$last").withOutputStream { output ->
	                    new File(directory+"/"+last).withInputStream { input ->
	                        byte[] buf = new byte[1024]
	                        while(true) {
	                            int len = input.read(buf)
	                            if (len == -1) break
	                            output.write(buf, 0, len)
	                        }
	                    }
	                }
	            }
	            println "$jpg has $timestamp ($last had $lastTimestamp)"
	            lastTimestamp = timestamp
	            last = jpg
	        }
		}
	}
}