$version = '3.2.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '68C23502C94F81ECFA1443D379CDF773F025287FA3026D43AE9F8697AAA01285'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
