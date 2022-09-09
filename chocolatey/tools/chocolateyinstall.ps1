$version = '3.6.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '86C8727DF355F259F75D2714B264C9D3FFAB36CCB7A3CE47BA062BF04FA6F03D'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
