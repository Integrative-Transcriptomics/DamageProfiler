#Replace version information appropriately in PKGBUILD and starterScript
VERSION=$(grep -e '^version' ../build.gradle | tr -d "'" | tr -d "version ")
VERSIONREL=1
#PKGBUILD
sed -i "s/^pkgver=.*/pkgver=$VERSION/" PKGBUILD
sed -i "s/^pkgrel=.*/pkgrel=$VERSIONREL/" PKGBUILD
#startscript
sed -i "s/^VERSION=.*/VERSION=$VERSION/" starter.sh

cp ../build/libs/*.jar .
makepkg
