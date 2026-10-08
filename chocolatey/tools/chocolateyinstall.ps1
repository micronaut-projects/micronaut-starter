$version = '5.2.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '13FA0341358320E5A2CA4BD50DC5EA4E6AC87240A448AA86D91468CA3C2FBA64'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
