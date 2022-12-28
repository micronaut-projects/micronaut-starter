$version = '3.8.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8E340C3E1904D756BBEC377391757A595D185E4B57C92EB57D1C3D1D73F48B76'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
