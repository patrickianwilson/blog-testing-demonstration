echo "Setting Up Workstation for Java Development"


#Ensure HomeBrew is installed.  This is the package manager of choice for Mac OS
command -v brew >/dev/null 2>&1 || { echo >&2 "Homebrew is required for these setup scripts but it's not installed.  Aborting."; exit 1; }


#Is Go already installed?
command -v java >/dev/null 2>&1 || { 
	echo >&2 "Installing Java from Home Brew"
	brew update
	brew cask install java
	brew install jenv
}



#Sublime is the editor of choice for Golang programmers.
command -v subl >/dev/null 2>&1 || { echo >&2 "Sublime Text (3) is Not Properly Installed, Please Install from: http://www.sublimetext.com"; echo "you must also link the 'subl' command to your path: sudo ln -s \"/Applications/Sublime Text.app/Contents/SharedSupport/bin/subl\" /usr/local/bin/subl"; read -p "Press [Enter] Once the Above is complete..."; }


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