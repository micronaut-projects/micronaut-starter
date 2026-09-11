$version = '5.1.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '038FC366174D93DA47FF815D6A79C5E3805D56B391FA2D0871561562ACDE1139'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
