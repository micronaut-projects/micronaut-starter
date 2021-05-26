$version = '2.5.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '6754C72A3074ECBCBA88467F6DC7C4399C5DA765A9DA9FA6153477A410BDFE55'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
