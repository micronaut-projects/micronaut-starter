$version = '4.7.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '9A5C0957D54FC7FA20B18BF939A36E3EBAA64FFC107FF15B21E944F38424C7AB'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
