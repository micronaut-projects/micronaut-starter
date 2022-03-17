$version = '3.3.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'A556825B3042508443817A73B566D885812DE93EF77DF05EDF78145345DDDDDB'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
