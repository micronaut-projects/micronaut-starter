$version = '5.0.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D37B9E6D83E35D80996D58BDC5FC15A3A8E90862BFAE4A67359A8769F260D117'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
