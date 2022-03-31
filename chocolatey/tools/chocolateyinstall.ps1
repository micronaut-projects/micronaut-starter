$version = '3.4.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '088AAE41637A7B2D38B4B5747F7CF5EF182DC01B56EC1898239721674A848779'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
