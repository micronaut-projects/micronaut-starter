$version = '3.7.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '29BD97F5C3575CB845FBCBA0F988F60D836FFDEEBEBE0E44FC1010A2F7E292A0'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
