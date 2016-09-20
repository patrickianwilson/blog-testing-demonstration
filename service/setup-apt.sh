echo "Setting Up Workstation for Java Development"

#Ensure aptitude is actually tnstalled.  This is the package manager of choice for Ubuntu
command -v apt-get >/dev/null 2>&1 || { echo >&2 "Aptitude is required for these setup scripts but it's not installed.  Aborting."; exit 1; }


#Is Go already installed?
command -v java >/dev/null 2>&1 || { 
	echo >&2 "Installing Java from Aptitude"
	sudo apt-add-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install oracle-java8-installer

}

#Sublime is the editor of choice for Java and Scripting programmers.
command -v subl >/dev/null 2>&1 || { 
	echo >&2 "Installing Sublime Text 3."
	sudo add-apt-repository ppa:webupd8team/sublime-text-3
	sudo apt-get update
	sudo apt-get install sublime-text-installer

}

ROOT_PROJECT=`pwd`
#gradle is the build system of choice for java projects (including Android)
command -v gradle >/dev/null 2>&1 || {
	echo "Installing Latest Gradle"

	curl https://downloads.gradle.org/distributions/gradle-2.10-bin.zip > temp.zip
	unzip temp.zip
	sudo cp -r gradle-2.10 /opt/
	sudo chmod -R 755 /opt/gradle-2.10
	sudo rm -rf temp.zip gradle-2.10
	sudo ln -s /opt/gradle-2.10/bin/gradle /usr/bin/gradle
}

cd $ROOT_PROJECT