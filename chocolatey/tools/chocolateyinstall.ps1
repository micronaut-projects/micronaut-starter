$version = '4.1.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B8B270348826CA8032DAC9B400C7D693CC6E72F31DE5C377A39D756C8A5D8BCA'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
